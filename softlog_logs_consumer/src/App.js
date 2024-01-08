import logo from './logo.svg';
import './App.css';

import React, { useEffect, useState } from 'react';
import MostReadsComponent from './Components/MostReadsComponent';
import {buildUserProfiles, mostWrites, mostReads, mostExpensiveProductSearched, mostSearchedProducts} from "./Controllers/UserProfilesController";
import axios from 'axios';
import MostWritesComponent from "./Components/MostWritesComponent";

// console.log(data);
const App = () => {
    const [dbData, setDistantData] = useState([]);
    const [localData, setLocalData] = useState([]);
    const [userProfiles, setUserProfiles] = useState([]);
    const [userProfilesArr, setUserProfilesArr] = useState([]);
    const fetchDistantData = () => {
        axios.get('http://localhost:8080/api/users/data')
            .then(response => {
                setDistantData(response.data); // Update fetched data in state
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    };

    const fetchLocalData = () => {
        fetch('./application.log')
            .then((res) => res.text())
            .then(text  => {
                let lines = [];
                for (const line of text.split("\n").filter(e => e !== "")) {
                    lines.push(JSON.parse(line));
                }
                setLocalData(lines);
                let tmpUsersProfiles = buildUserProfiles(lines);
                setUserProfiles(tmpUsersProfiles);
                setUserProfilesArr(Object.entries(userProfiles));
                const lol = Object.entries(userProfiles);
                console.log(mostWrites(lol));
                console.log(mostReads(lol));
                console.log(mostExpensiveProductSearched(lol));
                console.log(mostSearchedProducts(lol));
                // console.log(mostWrites(userProfiles));
            })
    };

    useEffect(() => {
        // Initial data fetch when component mounts
        fetchLocalData();
    }, []);

    return (
        <div className="App">
            <img src={logo} className="App-logo" alt="logo" width="300" height="300"/>
            <h1>Log Display App</h1>
            {/*{JSON.stringify(userProfiles)}*/}
            {/*{JSON.stringify(dbData)}*/}
            <div className="center space">
                <button className="BUTTON_HOF" onClick={fetchLocalData}>Fetch Local Data</button>
                <button className="BUTTON_HOF" onClick={fetchDistantData}>Fetch Distant Data</button>
            </div>
            <div className="card-container">
                <div className="card">
                    <MostReadsComponent retrievedUsers={mostReads(userProfilesArr)} userType={'Users with the most read operations'}/>
                </div>
                <div className="card">
                    <MostWritesComponent retrievedUsers={mostWrites(userProfilesArr)} userType={'Users with the most write operations'}/>
                </div>
                {/*<div className="card">*/}
                {/*    <MostReadsComponent retrievedUsers={mostExpensiveProductSearched(userProfilesArr)} userType={'Users that searched most expensive products'}/>*/}
                {/*</div>*/}
                {/*<div className="card">*/}
                {/*    <MostReadsComponent retrievedUsers={mostSearchedProducts(userProfilesArr)} userType={'Most searched products by users'}/>*/}
                {/*</div>*/}
            </div>
        </div>
    );
};

export default App;