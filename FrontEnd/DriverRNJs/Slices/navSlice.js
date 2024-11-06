import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  currentLocation: null,
  userInfo: null,
  tripreqList: [],
};

export const navSlice = createSlice({
  name: "nav",
  initialState,
  reducers: {
    setCurrentLocation: (state, action) => {
      state.currentLocation = action.payload;
    },
    setUserInfo: (state, action) => {
      state.userInfo = action.payload;
    },
    addTripreqList: (state, action) => {
      state.tripreqList.push(action.payload);
    },
    clearTripreqList: (state) => {
      state.tripreqList = [];
    },
  },
});

export const { setCurrentLocation, setUserInfo, addTripreqList, clearTripreqList} = navSlice.actions;

export const selectCurrentLocation = (state) => state.nav.currentLocation;
export const selectUserInfo = (state) => state.nav.userInfo;
export const selectTripreqList = (state) => state.nav.userInfo; 

export default navSlice.reducer;
