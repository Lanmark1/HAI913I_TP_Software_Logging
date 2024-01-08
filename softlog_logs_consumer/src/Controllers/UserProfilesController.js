const dictNames = {
    "READ" : "readOperations",
    "CREATE" : "createOperations",
    "UPDATE" : "updateOperations",
    "DELETE" : "deleteOperations",
};

export function buildUserProfiles(lines) {
    let tmpUsersProfiles = {};
    for (const line of lines) {
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
    return tmpUsersProfiles;
}

export function mostWrites(userProfilesArr) { // [0] : key, [1] : usable user info
    return userProfilesArr.toSorted((user1, user2) => {
        const sumU1 = user1[1].createOperations + user1[1].deleteOperations + user1[1].updateOperations;
        const sumU2 = user2[1].createOperations + user2[1].deleteOperations + user2[1].updateOperations;
        return sumU2 - sumU1;
    });
}

export function mostReads(userProfilesArr) {
    return userProfilesArr.toSorted((user1, user2) => {
        return user2[1].readOperations - user1[1].readOperations;
    });
}

export function mostExpensiveProductSearched(userProfilesArr) {
    return userProfilesArr.toSorted((user1, user2) => {
        let maxValU1 = 0;
        let maxValU2 = 0;

        if (user1[1].searchedProducts.length !== 0) {
            maxValU1 = user1[1].searchedProducts.reduce((max, obj) => {
                return obj.price > max ? obj.price : max;
            }, user1[1].searchedProducts[0].price);
        }
        if (user2[1].searchedProducts.length !== 0) {
            maxValU2 = user2[1].searchedProducts.reduce((max, obj) => {
                return obj.price > max ? obj.price : max;
            }, user2[1].searchedProducts[0].price);
        }
        return maxValU2 - maxValU1;
    });
}

export function mostSearchedProducts(userProfilesArr) {
    let products = {}
    for (const userProfile of userProfilesArr) {
        if (userProfile[1].searchedProducts.length !== 0) {
            for (const searchedProduct of userProfile[1].searchedProducts) {
                if (!products[searchedProduct.id])
                    products[searchedProduct.id] = {
                        product : searchedProduct,
                        occurrences : 0,
                    };
                products[searchedProduct.id].occurrences++;
            }
        }
    }
    return Object.entries(products).toSorted((prod1, prod2) => {
        return prod2[1].occurrences - prod1[1].occurrences;
    })
}