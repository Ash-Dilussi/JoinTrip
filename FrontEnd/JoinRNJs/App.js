import { Platform, StyleSheet, Text, View } from 'react-native';
import {Provider} from "react-redux";
import { store } from './store';
import HomeScreen from './Screens/HomeScreen';
import MapScreen from './Screens/MapScreen';
import { SafeAreaProvider } from 'react-native-safe-area-context';
import 'react-native-gesture-handler';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import Geolocation from 'react-native-geolocation-service';
import { KeyboardAvoidingView } from 'react-native';
export default function App() {
const Stack = createStackNavigator();


  return (
    <Provider store={store} >

      
      <NavigationContainer>
      <SafeAreaProvider>

<KeyboardAvoidingView
behavior={Platform.OS === "android"? "height": "padding"}
style = {{ flex:1}}>

<Stack.Navigator>
          <Stack.Screen
          name = 'HomeScreen'
          component={HomeScreen}
          options={
            {headerShown: false}
          }
          />
          <Stack.Screen
          name = 'MapScreen'
          component={MapScreen}
          options={
            {headerShown: false}
          }
          />

        </Stack.Navigator>
</KeyboardAvoidingView>
        
      
      </SafeAreaProvider>
      </NavigationContainer>
    </Provider>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
