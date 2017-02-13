package net.nutrima.nutrimaprotogui;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;

import net.nutrima.aws.RestaurantMenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by melsisi on 2/1/2017.
 */

public interface LambdaInterface {

    @LambdaFunction
    RestaurantMenuItem[] HelloFunction(LambdaRequest request);

    @LambdaFunction
    LambdaRespMenues GetFullAndFilteredMenuForRestaurant(String input);
}
