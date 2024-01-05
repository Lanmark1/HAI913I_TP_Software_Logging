import logo from './logo.svg';
import './App.css';

import React, { useEffect, useState } from 'react';
import LogComponent from './Components/LogComponent';
import axios from 'axios';

const App = () => {
    const logs = ["user 1", "user 2"];
    const [data, setData] = useState([]);
    const test = [data[0].user.name];
    const fetchData = () => {
        axios.get('http://localhost:8080/api/users/data')
            .then(response => {
                console.log(response);
                console.log(JSON.stringify(response.data[0].user.name));
                setData(response.data); // Update fetched data in state
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    };

    useEffect(() => {
        // Initial data fetch when component mounts
        fetchData();
    }, []);

    return (
        <div className="App">
            <img src={logo} className="App-logo" alt="logo" width="300" height="300"/>
            <h1>Log Display App</h1>
            {JSON.stringify(data)}
            <div className="card-container">
                <button onClick={fetchData}>Fetch Data</button>
                <div className="card">
                    <LogComponent retrievedUsers={test} userType={'most read operations'}/>
                </div>
                <div className="card">
                    <LogComponent retrievedUsers={logs} userType={'most write operations'}/>
                </div>
                <div className="card">
                    <LogComponent retrievedUsers={logs} userType={'most expensive product searched'}/>
                </div>
            </div>
        </div>
    );
};

export default App;