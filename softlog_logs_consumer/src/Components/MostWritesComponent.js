import React from "react";

const MostWritesComponent = ({ retrievedUsers, userType }) => {
    if (!retrievedUsers || !userType) {
        return <div>Loading...</div>;
    }
    return (
        <div>
            <h2 className="card-title">{userType}</h2>
            {
                retrievedUsers.map((userProf, index) => {
                    if (userProf[1].createOperations !== 0 ||
                        userProf[1].updateOperations !== 0 ||
                        userProf[1].deleteOperations !== 0)
                    return(
                        <div key={index}>
                            <p>{userProf[1].user.name} (created : {userProf[1].createOperations}, updated
                                : {userProf[1].updateOperations}, deleted : {userProf[1].deleteOperations})</p>
                        </div>);
                })
            }
        </div>
    );
};
export default MostWritesComponent;