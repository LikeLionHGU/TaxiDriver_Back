package hgu.likelion.fish.ai;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class InferenceResult {
    private String status;
    private boolean is_normal;
    private String final_diagnosis;
    private String predicted_class;
    private double confidence;
    private double mahalanobis_distance;
    private double euclidean_distance;
    private double min_class_distance;
    private String nearest_class;
    private double feature_norm;
    private double entropy;
    private List<String> decision_reasons;
    private Map<String, Double> all_distances;

    // getter/setter or @Data (Lombok)
}
