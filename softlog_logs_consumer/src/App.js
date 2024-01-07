import logo from './logo.svg';
import './App.css';

import React, { useEffect, useState } from 'react';
import LogComponent from './Components/LogComponent';
import axios from 'axios';

// console.log(data);
const App = () => {
    const logs = ["user 1", "user 2"];
    const [dbData, setDistantData] = useState([]);
    const [localData, setLocalData] = useState('');
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
                setLocalData(text);
            })
    };

    return (
        <div className="App">
            <img src={logo} className="App-logo" alt="logo" width="300" height="300"/>
            <h1>Log Display App</h1>
            {localData}
            {JSON.stringify(dbData)}
            <div className="card-container">
                <button onClick={fetchLocalData}>Fetch Local Data</button>
                <button onClick={fetchDistantData}>Fetch Distant Data</button>
            </div>
            <div className="card">
                <LogComponent retrievedUsers={logs} userType={'most read operations'}/>
            </div>
            <div className="card">
                <LogComponent retrievedUsers={logs} userType={'most write operations'}/>
            </div>
            <div className="card">
                <LogComponent retrievedUsers={logs} userType={'most expensive product searched'}/>
            </div>
        </div>
    );
};

export default App;