package hgu.likelion.fish.ai;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;


@Data
@NoArgsConstructor
public class InferenceResult {
    private String status;

    @JsonProperty("is_normal")
    private boolean isNormal;

    @JsonProperty("final_diagnosis")
    private String finalDiagnosis;

    @JsonProperty("predicted_class")
    private String predictedClass;

    private double confidence;

    @JsonProperty("mahalanobis_distance")
    private double mahalanobisDistance;

    @JsonProperty("euclidean_distance")
    private double euclideanDistance;

    @JsonProperty("min_class_distance")
    private double minClassDistance;

    @JsonProperty("nearest_class")
    private String nearestClass;

    @JsonProperty("feature_norm")
    private double featureNorm;

    private double entropy;

    @JsonProperty("decision_reasons")   // ← 문자열임
    private String decisionReasons;

    @JsonProperty("all_distances")
    private Map<String, Double> allDistances;
}
