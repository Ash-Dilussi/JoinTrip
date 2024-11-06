import { Platform, StyleSheet, Text, View } from "react-native";

import LoginScreen from "./Screens/LoginScreen";
import HomeScreen from "./Screens/HomeScreen";
import RegScreen from "./Screens/RegScreen";
import ToMapScreen from "./Screens/ToMapScreen";
import DistantRoute from "./Screens/DistantRoute";
import JoinListScreen from "./Screens/JoinListScreen";
import ScheduleScreen from "./Screens/ScheduleScreen";
import { SafeAreaProvider } from "react-native-safe-area-context";
import "react-native-gesture-handler";
import { NavigationContainer } from "@react-navigation/native";
import { createStackNavigator } from "@react-navigation/stack";
import Geolocation from "react-native-geolocation-service";
import { KeyboardAvoidingView } from "react-native";
import {Provider} from "react-redux";
import { store } from "./store";

export default function App() {
  const Stack = createStackNavigator();

  return (
    <Provider store={store}>
      <NavigationContainer>
        <SafeAreaProvider>
          <KeyboardAvoidingView
            behavior={Platform.OS === "android" ? "height" : "padding"}
            style={{ flex: 1 }}
          >
            <Stack.Navigator>
              <Stack.Screen
                name="LoginScreen"
                component={LoginScreen}
                options={{ headerShown: false }}
              />
              <Stack.Screen
                name="RegScreen"
                component={RegScreen}
                options={{ headerShown: false }}
              />
              <Stack.Screen
                name="HomeScreen"
                component={HomeScreen}
                options={{ headerShown: false }}
              />
              <Stack.Screen
                name="ToMapScreen"
                component={ToMapScreen}
                options={{ headerShown: false }}
              />
              <Stack.Screen
                name="DistantRoute"
                component={DistantRoute}
                options={{ headerShown: false }}
              />
              <Stack.Screen
                name="JoinListScreen"
                component={JoinListScreen}
                options={{ headerShown: false }}
              />
              <Stack.Screen
                name="ScheduleScreen"
                component={ScheduleScreen}
                options={{ headerShown: false }}
              />
            </Stack.Navigator>
          </KeyboardAvoidingView>
        </SafeAreaProvider>
      </NavigationContainer>
    </Provider>
  );
}
