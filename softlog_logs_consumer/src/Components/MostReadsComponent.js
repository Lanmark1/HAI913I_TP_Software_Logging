import React from 'react';

const MostReadsComponent = ({ retrievedUsers, userType }) => {
    if (!retrievedUsers || !userType) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <h2 className="card-title">{userType}</h2>
            {
                retrievedUsers.map((userProf, index) => {
                    if (userProf[1].readOperations !== 0)
                        return(
                        <div key={index}>
                            <p>{userProf[1].user.name} (read : {userProf[1].readOperations})</p>
                        </div>);
                })
            }
        </div>
    );
};

export default MostReadsComponent;