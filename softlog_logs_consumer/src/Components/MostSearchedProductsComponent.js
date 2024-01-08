import React from 'react';

const MostSearchedComponent = ({ retrievedProducts, userType }) => {
    if (!retrievedProducts || !userType) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <h2 className="card-title">{userType}</h2>
            {
                retrievedProducts.map((product, index) => (
                    <div key={index}>
                        <p>{product[1].product.name}, searched : {product[1].occurrences} time(s)</p>
                    </div>
                ))
            }
        </div>
    );
};

export default MostSearchedComponent;