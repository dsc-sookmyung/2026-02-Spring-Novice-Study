
// ==================== 삭제 기능 ====================
const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
    deleteButton.addEventListener('click', event => {
        let id = document.getElementById('article-id').value;

        function success() {
            alert('삭제가 완료되었습니다.');
            location.replace('/articles');
        }

        function fail() {
            alert('삭제 실패했습니다.');
        }

        httpRequest('DELETE', `/api/articles/${id}`, null, success, fail);
    });
}


// ==================== 수정 기능 ====================
const modifyButton = document.getElementById('modify-btn');

if (modifyButton) {
    modifyButton.addEventListener('click', event => {
        let params = new URLSearchParams(location.search);
        let id = params.get('id');

        const body = JSON.stringify({
            title: document.getElementById('title').value,
            content: document.getElementById('content').value
        });

        function success() {
            alert('수정이 완료되었습니다.');
            location.replace(`/articles/${id}`);
        }

        function fail() {
            alert('수정 실패했습니다.');
        }

        httpRequest('PUT', `/api/articles/${id}`, body, success, fail);
    });
}


// ==================== 등록 기능 ====================
const createButton = document.getElementById('create-btn');

if (createButton) {
    createButton.addEventListener('click', event => {

        const body = JSON.stringify({
            title: document.getElementById('title').value,
            content: document.getElementById('content').value
        });

        function success() {
            alert('등록이 완료되었습니다.');
            location.replace('/articles');
        }

        function fail() {
            alert('등록 실패했습니다.');
        }

        httpRequest('POST', '/api/articles', body, success, fail);
    });
}


// ==================== 쿠키 조회 ====================
function getCookie(key) {
    const cookies = document.cookie.split(';');

    for (const item of cookies) {
        const [name, ...value] = item.trim().split('=');

        if (name === key) {
            return value.join('=');
        }
    }

    return null;
}


// ==================== JWT 인증 요청 ====================
async function httpRequest(method, url, body, success, fail) {
    try {
        let accessToken = localStorage.getItem('access_token');

        const sendRequest = token => fetch(url, {
            method: method,
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json'
            },
            body: body
        });

        let response = await sendRequest(accessToken);

        // 액세스 토큰 만료 시 재발급
        if (response.status === 401) {
            const refreshToken = getCookie('refresh_token');

            if (!refreshToken) {
                fail();
                return;
            }

            const tokenResponse = await fetch('/api/token', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    refreshToken: refreshToken
                })
            });

            if (!tokenResponse.ok) {
                fail();
                return;
            }

            const result = await tokenResponse.json();

            accessToken = result.accessToken;
            localStorage.setItem('access_token', accessToken);

            // 새 토큰으로 기존 요청 재시도
            response = await sendRequest(accessToken);
        }

        if (response.ok) {
            success();
        } else {
            fail();
        }

    } catch (error) {
        console.error('API 요청 오류:', error);
        fail();
    }
}
