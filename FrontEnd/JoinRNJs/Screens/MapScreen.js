import React from 'react'
import { FlatList, Image, SafeAreaView, StyleSheet, Text, TouchableOpacity, View } from 'react-native'

import tw from 'twrnc';
import Map from '../Components/Map';
import MapNavigationCard from '../Components/MapNavigationCard';
import MapView from 'react-native-maps';
import SelectOptionCard from '../Components/SelectOptionCard';
import { createStackNavigator } from '@react-navigation/stack';


const MapScreen = () => {

  const Stack= createStackNavigator();
  return (
    <View>


      <View style={tw` h-1/2`}>
        <Map />
      </View>


      <View style={tw`h-1/2`}>

        <Stack.Navigator>
          <Stack.Screen
            name="MapNavigationCard"
            component={MapNavigationCard}
            options={{
              headerShown: false,
            }}
          />
          <Stack.Screen
            name="SelectOptionCard"
            component={SelectOptionCard}
            options={{
              headerShown: false,
            }}
          />

        </Stack.Navigator>

      </View>


    </View>
  )
}


export default MapScreen

const styles = StyleSheet.create({

})