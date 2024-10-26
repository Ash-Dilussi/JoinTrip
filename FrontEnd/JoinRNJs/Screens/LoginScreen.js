import React, { useState } from "react";
import { View, Text, Image, TextInput, TouchableOpacity } from "react-native";
import tw from "twrnc";
import { useDispatch, useSelector } from "react-redux";
import { setUserType } from "../slices/navSlice";

const LoginScreen = ({ navigation }) => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const dispatch = useDispatch();

  const handleLogin = () => {
    // Handle login logic here
    dispatch(
      setUserType({
        type: 1,
      })
    );
    console.log("Logging in with", username, password);
  };

  const handleGuestLogin = () => {
   
    dispatch(
      setUserType({
        type: 2,
      })
    );
    navigation.navigate("RegScreen");
    console.log("User type as guest");
  };

  return (
    <View style={tw`flex-1 justify-center items-center bg-gray-100`}>
      <Image
        source={require("../assets/JointripLogo.png")}
        style={tw`w-40 h-40`}
        resizeMode="contain"
      />
      <Text style={tw`text-lg text-gray-500 mt-4 mb-5`}>Welcome to MyApp</Text>

      <TextInput
        style={tw`border border-gray-300 rounded-lg px-4 py-2 mb-4 w-80`}
        placeholder="Username"
        value={username}
        onChangeText={setUsername}
      />

      <TextInput
        style={tw`border border-gray-300 rounded-lg px-4 py-2 mb-6 w-80`}
        placeholder="Password"
        secureTextEntry
        value={password}
        onChangeText={setPassword}
      />

      <TouchableOpacity
        style={tw`bg-blue-500 rounded-lg px-4 py-2 w-80 mb-4`}
        onPress={handleLogin}
      >
        <Text style={tw`text-white text-center`}>Login</Text>
      </TouchableOpacity>

      <TouchableOpacity
        style={tw`bg-gray-500 rounded-lg px-4 py-2 w-80 mb-4`}
        onPress={handleGuestLogin}
      >
        <Text style={tw`text-white text-center`}>Enter as Guest</Text>
      </TouchableOpacity>

      <TouchableOpacity onPress={() => navigation.navigate("RegScreen")}>
        <Text style={tw`text-blue-500`}>Register</Text>
      </TouchableOpacity>
    </View>
  );
};

export default LoginScreen;
