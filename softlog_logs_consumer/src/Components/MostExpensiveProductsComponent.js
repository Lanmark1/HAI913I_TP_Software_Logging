import React, { useEffect, useState } from 'react';

const MostExpensiveProductsComponent = ({ retrievedUsers, userType }) => {
    if (!retrievedUsers || !userType) {
        return <div>Loading...</div>;
    }

    const mostExpensiveProduct = (userProf) => {
        if (userProf[1].searchedProducts.length !== 0) {
            return (userProf[1].searchedProducts.reduce((maxObj, obj) => {
                return obj.price > maxObj.price ? obj : maxObj;
            }, userProf[1].searchedProducts[0]));
        }
        else {
            return {};
        }
    }

    function isEmpty(obj) {
        return Object.keys(obj).length === 0;
    }

    return (
        <div>
            <h2 className="card-title">{userType}</h2>
            {
                retrievedUsers.map((userProf, index) => {
                    let expensiveProduct = mostExpensiveProduct(userProf);
                    if (!isEmpty(expensiveProduct)) {
                        return (
                            <div key={index}>
                                <p>{userProf[1].user.name} (product : {expensiveProduct.name}, price : {expensiveProduct.price})</p>
                            </div>
                        );
                    }
                })
            }
        </div>
    );
};

export default MostExpensiveProductsComponent;