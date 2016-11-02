package net.nutrima.engine;

/**
 * Created by ayehia on 10/3/2016.
 */

public class EngineUtils {

    int GenericCalBurnt (UserProfile userProfile,  int avgHeartRate, int exerciseDuration) {
        if (userProfile == null)
                return -1;
        if (userProfile.getMetricImperial() == MetricStandard.METRIC) {
            if (userProfile.getGender() == Gender.MALE) {
                return ((int) (((-55.0969 + (0.6309 * avgHeartRate) +
                                (0.1988 * userProfile.getWeight()) +
                                (0.2017 * userProfile.getAge()))/4.184) * exerciseDuration));

            } else {
                return ((int) (((-20.4022 + (0.4472 * avgHeartRate) +
                        (0.126 * userProfile.getWeight()) +
                        (0.074 * userProfile.getAge()))/4.184) * exerciseDuration));
            }
        } else {
            if (userProfile.getGender() == Gender.MALE) {
                return ((int) (((-55.0969 + (0.6309 * avgHeartRate) +
                        (0.1988 * userProfile.getWeight()*0.453592) +
                        (0.2017 * userProfile.getAge()))/4.184) * exerciseDuration));

            } else {
                return ((int) (((-20.4022 + (0.4472 * avgHeartRate) +
                        (0.126 * userProfile.getWeight()*0.453592) +
                        (0.074 * userProfile.getAge()))/4.184) * exerciseDuration));
            }
        }
    }
}
