/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tomocomd.subsetsearch.evaluation.subsetevaluation;

import java.util.Arrays;
import java.util.BitSet;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import tomocomd.configuration.evaluation.subsetevaluation.SubsetEvaluationConfig;
import tomocomd.data.PopulationInstances;
import tomocomd.exceptions.AExOpDCSException;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.SubsetEvaluator;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

/**
 * @author potter
 */
@Slf4j
@Data
public class EvaluateSubset extends AttributeSelection implements IEvaluateSubset {

    private static final String MSG_BUILD_ERROR = "Problems selecting attributes";
    private static final String MSG_EVA_ERROR = "Problems getting selected attribute index";
    private static final String MSG_SEARCH_ERROR = "Error getting best attributes";
    private static final String MSG_ERROR = "Error searching best subset";

    SubsetEvaluationConfig conf;

    public EvaluateSubset(SubsetEvaluationConfig conf) {
        this.conf = conf;
    }

    private void setEvaluatorAndSearchMethods() {
        ASEvaluation eva = SubsetFitnessFactory.getSubsetEvaluator(conf.getSubSetFitnessConf().getType());
        setSearch(SubsetSearchFactory.getSearchMethod(conf.getSubsetSearchConf().getSubsetSearchType()));
        setEvaluator(eva);
        setXval(false);
    }

    private PopulationInstances handleException(String msg, Exception ex,  PopulationInstances inst, long startTime) throws AExOpDCSException {
        log.error(
                "Error: {}, for a data of {} attributes, returning an empty subset with merit = 0",
                msg,
                inst.numAttributes(),
                ex);
        try {
            Remove rem = new Remove();
            rem.setAttributeIndicesArray(new int[]{inst.classIndex()});
            rem.setInvertSelection(true);
            rem.setInputFormat(inst);

            PopulationInstances instRes = new PopulationInstances(Filter.useFilter(inst, rem));
            instRes.setEvaSub(0);
            showFinishMsg(startTime, inst.numAttributes());
            return instRes;
        } catch (Exception exception) {
            throw AExOpDCSException.ExceptionType.SUBSET_EVALUATE_EXCEPTION.get(MSG_ERROR, exception);
        }
    }


    public PopulationInstances bestSubset(PopulationInstances inst) throws AExOpDCSException {

        long startTime = System.currentTimeMillis();
        setEvaluatorAndSearchMethods();

        int[] selectedAttributes;
        try {
            SelectAttributes(inst);
        }  catch (Exception ex) {
            return handleException(MSG_BUILD_ERROR,ex,inst, startTime);
        }

        try {
            selectedAttributes = selectedAttributes();
        } catch (Exception ex) {
            return handleException(MSG_EVA_ERROR,ex,inst, startTime);
        }

        double value = getMeritValue(inst, selectedAttributes);

        Arrays.sort(selectedAttributes);
        return filterSelectedAttributes(inst, startTime, selectedAttributes, value);
    }

    private PopulationInstances filterSelectedAttributes(PopulationInstances inst, long startTime, int[] selectedAttributes, double meritValue) throws AExOpDCSException {
        Remove rem = new Remove();
        rem.setAttributeIndicesArray(selectedAttributes);
        rem.setInvertSelection(true);

        try {
            rem.setInputFormat(inst);
            PopulationInstances instRes = new PopulationInstances(Filter.useFilter(inst, rem));
            instRes.setEvaSub(meritValue);
            showFinishMsg(startTime, inst.numAttributes());
            return instRes;
        } catch (Exception ex) {
            return handleException(MSG_SEARCH_ERROR,ex,inst, startTime);
        }
    }

    private double getMeritValue(PopulationInstances inst, int[] selectedAttributes) {
        double meritValue;
        try {
            meritValue = getMeritFromStringResult();
        } catch (NumberFormatException ex) {
            meritValue = getMeritSubset(inst, selectedAttributes);
        }
        return meritValue;
    }

    private void showFinishMsg(Long startTime, int numAttributes) {
        log.debug(
                "Best subset search performed in {} ms for {} molecular descriptors",
                System.currentTimeMillis() - startTime,
                numAttributes);

    }

    private double getMeritFromStringResult() throws NumberFormatException {
        String[] lines = toResultsString().split("\n");
        for (String line : lines) {
            if (line.contains("Merit of best subset found")) {
                String valueS = line.split(":")[1];
                return Double.parseDouble(valueS);
            }
        }
        return Double.NaN;
    }

    public double getMeritSubset(PopulationInstances inst, int[] pos) throws AExOpDCSException {
        ASEvaluation eva =
                SubsetFitnessFactory.getSubsetEvaluator(conf.getSubSetFitnessConf().getType());

        BitSet bitBest = new BitSet(inst.numAttributes());
        if (pos.length == 0) {
            for (int i = 0; i < inst.numAttributes(); i++) {
                if (i != inst.classIndex()) {
                    bitBest.set(i);
                }
            }
        } else {
            for (int i : pos) {
                if (i != inst.classIndex()) {
                    bitBest.set(i);
                }
            }
        }

        try {
            eva.buildEvaluator(inst);
            return ((SubsetEvaluator) (eva)).evaluateSubset(bitBest);
        } catch (Exception ex) {
            throw AExOpDCSException.ExceptionType.SUBSET_EVALUATE_EXCEPTION.get(
                    "Problems evaluating fitness subet", ex);
        }
    }
}
