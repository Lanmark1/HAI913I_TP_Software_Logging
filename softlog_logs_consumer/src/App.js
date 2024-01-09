import logo from './logo.svg';
import './App.css';

import React, { useEffect, useState } from 'react';
import MostReadsComponent from './Components/MostReadsComponent';
import {
    buildUserProfiles,
    mostWrites,
    mostReads,
    mostExpensiveProductSearched,
    mostSearchedProducts,
    convertDistantToLocalFormat
} from "./Controllers/UserProfilesController";
import axios from 'axios';
import MostWritesComponent from "./Components/MostWritesComponent";
import MostExpensiveProductsComponent from "./Components/MostExpensiveProductsComponent";
import MostSearchedProductsComponent from "./Components/MostSearchedProductsComponent";

const App = () => {
    const [userProfilesArr, setUserProfilesArr] = useState([]);
    const fetchDistantData = () => {
        axios.get('http://localhost:8080/api/users/data')
            .then(response => {
                console.log("distant (before treatment)");
                console.log(response.data);
                console.log("distant (after treatment)");
                console.log(convertDistantToLocalFormat(response.data));
                setUserProfilesArr(convertDistantToLocalFormat(response.data));

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
                let userProfiles = buildUserProfiles(lines);
                console.log("local");
                console.log(Object.entries(userProfiles));
                setUserProfilesArr(Object.entries(userProfiles));
            })
    };

    useEffect(() => {
        document.title = "Logs Consumer App"
        fetchLocalData();
    }, []);

    return (
        <div className="App">
            <img src={logo} className="App-logo" alt="logo" width="300" height="300"/>
            {/*<h1>Log Consumer App</h1>*/}
            <div className="center space">
                <button className="BUTTON_HOF" onClick={fetchLocalData}>Use data from the local logs</button>
                <button className="BUTTON_HOF" onClick={fetchDistantData}>Use data from the API</button>
            </div>
            <div className="card-container">
                <div className="card">
                    <MostReadsComponent retrievedUsers={mostReads(userProfilesArr)} userType={'Users with the most read operations'}/>
                </div>
                <div className="card">
                    <MostWritesComponent retrievedUsers={mostWrites(userProfilesArr)} userType={'Users with the most write operations'}/>
                </div>
                <div className="card">
                    <MostExpensiveProductsComponent retrievedUsers={mostExpensiveProductSearched(userProfilesArr)} userType={'Users that searched the most expensive products'}/>
                </div>
                <div className="card">
                    <MostSearchedProductsComponent retrievedProducts={mostSearchedProducts(userProfilesArr)} userType={'Most searched products by users'}/>
                </div>
            </div>
        </div>
    );
};

export default App;