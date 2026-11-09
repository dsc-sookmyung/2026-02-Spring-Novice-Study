const token = searchParam('token')


if (token) {
    localStorage("access_token", token)
}

function searchParam(key) {
    return new URLSearchParams(location.search).get(key);
}