import logo from './logo.svg';
import './App.css';

import React, { useEffect, useState } from 'react';
import LogComponent from './Components/LogComponent';
import axios from 'axios';

// console.log(data);
const App = () => {
    const logs = ["user 1", "user 2"];
    const [dbData, setDistantData] = useState([]);
    const [localData, setLocalData] = useState([]);
    const [userProfiles, setUserProfiles] = useState([]);
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
            })
    };

    const dictNames = {
        "READ" : "readOperations",
        "CREATE" : "createOperations",
        "UPDATE" : "updateOperations",
        "DELETE" : "deleteOperations",
    };
    const buildUserProfiles = async () => {
        await fetchLocalData();
        let tmpUsersProfiles = {};
        for (const line of localData) {
            if (line.user) {
                if (!tmpUsersProfiles[line.user.id]) {
                    tmpUsersProfiles[line.user.id] = {
                        'searchedProducts' : [],
                        'user' : structuredClone(line.user),
                        'readOperations' : 0,
                        'updateOperations' : 0,
                        'createOperations' : 0,
                        'deleteOperations' : 0
                    };
                }
                tmpUsersProfiles[line.user.id][dictNames[line.action]] += 1;
                if (line.searchedProduct)
                    tmpUsersProfiles[line.user.id].searchedProducts.push(/*[*/line.searchedProduct);//, new Date(line.timestamp)]);
            }
        }
        setUserProfiles(tmpUsersProfiles);
    }

    return (
        <div className="App">
            <img src={logo} className="App-logo" alt="logo" width="300" height="300"/>
            <h1>Log Display App</h1>
            {/*{JSON.stringify(userProfiles)}*/}
            {/*{JSON.stringify(dbData)}*/}
            <div className="card-container space">
                <button className="BUTTON_HOF" onClick={buildUserProfiles}>Fetch Local Data</button>
                <button className="BUTTON_HOF" onClick={fetchDistantData}>Fetch Distant Data</button>
            </div>
            <div className="card-container">
                <div className="card">
                    <LogComponent retrievedUsers={logs} userType={'Users with the most read operations'}/>
                </div>
                <div className="card">
                    <LogComponent retrievedUsers={logs} userType={'Users with the most write operations'}/>
                </div>
                <div className="card">
                    <LogComponent retrievedUsers={logs} userType={'Users that have searched the most expensive products'}/>
                </div>
                <div className="card">
                    <LogComponent retrievedUsers={logs} userType={'Most searched products'}/>
                </div>
            </div>
        </div>
    );
};

export default App;