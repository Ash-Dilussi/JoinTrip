import React from 'react'
import { SafeAreaView, StyleSheet, Text, View } from 'react-native'
import tw from 'twrnc'
import NavOptions from '../Components/NavOptions'
import { GooglePlacesAutocomplete } from 'react-native-google-places-autocomplete';
import { GOOGLE_MAPS_APIKEY } from "@env";
import { TextInput } from 'react-native-gesture-handler';
import { useDispatch } from 'react-redux';
import { setDestination, setOrigin } from '../slices/navSlice';



const HomeScreen = () => {

    const dispatch = useDispatch();

    return (
        <SafeAreaView style={tw`p-4 android:pt-2 bg-white dark:bg-black`}>
            <Text style={[tw`text-2xl text-black p-10`]}>Join Trip</Text>


            <GooglePlacesAutocomplete
                placeholder='You are at?'
                debounce={400}
                nearbyPlacesAPI="GooglePlacesSearch"
                styles={{container:{flex: 0}, textInput:{fontSize: 18, backgroundColor: "#F5F5F5"}}}


                 onPress={(data, details = null) => {
                     // 'details' is provided when fetchDetails = true
                     

                     dispatch(setOrigin({
                        location: details.geometry.location,
                        description: data.description
                     }))
                     console.log(data.description, details.geometry);
                     dispatch(setDestination(null));

                 }}
                 fetchDetails={true}
                 returnKeyType={"search"}
                query={{
                    key: GOOGLE_MAPS_APIKEY,
                    language: 'en',
                }}
            />
            <NavOptions />
        </SafeAreaView>
    )
}


export default HomeScreen


const styles = StyleSheet.create({

    text: {
        color: "blue",
    }
})