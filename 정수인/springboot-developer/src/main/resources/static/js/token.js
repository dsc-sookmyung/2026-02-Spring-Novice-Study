const token = searchParam('token');

if (token) {
    localStorage.setItem('access_token', token);
    window.history.replaceState({}, document.title, window.location.pathname);
}

function searchParam(key) {
    return new URLSearchParams(location.search).get(key);
}

function httpRequest(method, url, body, success, fail) {
    fetch(url, {
        method: method,
        headers: {
            Authorization: 'Bearer ' + localStorage.getItem('access_token'),
            'Content-Type': 'application/json'
        },
        body: body
    }).then(response => {
        if (response.status === 200 || response.status === 201) {
            return success();
        }

        if (response.status === 401) {
            fetch('/api/token', {
                method: 'POST',
                credentials: 'same-origin'
            })
                .then(res => {
                    if (!res.ok) {
                        throw new Error('토큰 재발급 실패');
                    }
                    return res.json();
                })
                .then(result => {
                    localStorage.setItem('access_token', result.accessToken);
                    httpRequest(method, url, body, success, fail);
                })
                .catch(error => fail());
        } else {
            return fail();
        }
    }).catch(error => fail());
}
