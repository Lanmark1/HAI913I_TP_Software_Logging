import React from 'react';



const LogComponent = ({ retrievedUsers, userType }) => {
    if (!retrievedUsers || !userType) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <h2>Users with the {userType}</h2>
            {retrievedUsers.map((user, index) => (
                <div key={index}>
                    <p>{user}</p>
                </div>
            ))}
        </div>
    );
};

export default LogComponent;