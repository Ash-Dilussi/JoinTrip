import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  currentLocation: null,
  userInfo: null,
};

export const navSlice = createSlice({
  name: "nav",
  initialState,
  reducers: {
    setCurrentLocation: (state, action) => {
      state.currentLocation = action.payload;
    },
    setUserInfo:(state,action)=>{
        state.userInfo = action.payload;
    },
  },
});

export const { setCurrentLocation, setUserInfo } = navSlice.actions;

export const selectCurrentLocation = (state) => state.nav.currentLocation;
export const selectUserInfo = (state) => state.nav.userInfo;


export default navSlice.reducer;

