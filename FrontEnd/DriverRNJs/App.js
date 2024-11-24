import { StatusBar } from "expo-status-bar";
import {Platform, StyleSheet, Text, View } from "react-native";
import { SafeAreaProvider } from "react-native-safe-area-context";
import "react-native-gesture-handler";
import { NavigationContainer } from "@react-navigation/native";
import { createStackNavigator } from "@react-navigation/stack";
import { Provider } from "react-redux";
import { store } from "./store";
import 'react-native-get-random-values';
import Geolocation from "react-native-geolocation-service";
import { KeyboardAvoidingView } from "react-native";
import LoginScreen from "./Screens/LoginScreen";
import StartTaxi from "./Screens/StartTaxi";
import RegScreen from "./Screens/RegScreen";
import TripRequestList from "./Screens/TripRequestList";


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
                name="StartTaxi"
                component={StartTaxi}
                options={{ headerShown: false }}
              />
              <Stack.Screen
                name="TripRequestList"
                component={TripRequestList}
                options={{ headerShown: false }}
              />
            </Stack.Navigator>
          </KeyboardAvoidingView>
        </SafeAreaProvider>
      </NavigationContainer>
    </Provider>
  );
}
