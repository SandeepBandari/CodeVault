// Login Form
const loginForm = document.getElementById('loginForm');
if (loginForm) {
    loginForm.addEventListener('submit', async function (e) {
        e.preventDefault();

        const username = document.getElementById('loginUsername').value.trim();
        const password = document.getElementById('loginPassword').value.trim();

        try {
            const response = await fetch('http://localhost:8080/api/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password }),
                credentials: 'include', // Include cookies for session-based authentication
            });

            if (response.ok) {
                const data = await response.json();
                alert(data.message || 'Login successful!');
                // Store credentials in sessionStorage
                sessionStorage.setItem('username', username);
                sessionStorage.setItem('role', data.role || 'USER');
                // Redirect to the calculator page
                window.location.href = 'calc.html';
            } else {
                const errorMessage = await response.text();
                document.getElementById('loginErrorMessage').textContent = errorMessage || 'Login failed';
            }
        } catch (error) {
            console.error('Error:', error);
            document.getElementById('loginErrorMessage').textContent = 'Error connecting to the server.';
        }
    });
}

// Register Form
const registerForm = document.getElementById('registerForm');
if (registerForm) {
    registerForm.addEventListener('submit', async function (e) {
        e.preventDefault();

        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        const role = document.getElementById('role').value;

        try {
            const response = await fetch('http://localhost:8080/api/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password, role }),
            });

            if (response.ok) {
                alert('Registration successful!');
                window.location.href = 'login.html';
            } else {
                const errorMessage = await response.text();
                document.getElementById('errorMessage').textContent = `Registration failed: ${errorMessage}`;
            }
        } catch (error) {
            console.error('Error:', error);
            document.getElementById('errorMessage').textContent = 'Error connecting to the server.';
        }
    });
}

// Calculator Functions
let input = '';

function prs_key(key) {
    input += key;
    updateDisplay();
}

function updateDisplay() {
    const displayElement = document.getElementById('display');
    if (displayElement) {
        displayElement.innerText = input || '';
    }
}

function clearDisplay() {
    input = '';
    updateDisplay();
}

function del() {
    input = input.slice(0, -1);
    updateDisplay();
}

async function calculate() {
    const username = sessionStorage.getItem('username');
    const password = sessionStorage.getItem('password');

    if (!username || !password) {
        alert('You are not logged in. Please log in first.');
        window.location.href = 'login.html';
        return;
    }

    const authHeader = `Basic ${btoa(username + ":" + password)}`;
    const operationMap = { '+': 'add', '-': 'sub', '*': 'mul', '/': 'div', '%': 'modulus' };
    const operation = input.match(/[+\-*/%]/)?.[0];

    if (!operation) {
        alert('Invalid operation');
        return;
    }

    const [value1, value2] = input.split(operation).map(Number);
    const operationName = operationMap[operation];

    try {
        const response = await fetch(`http://localhost:8080/api/${operationName}?value1=${value1}&value2=${value2}`, {
            method: 'POST',
            headers: {
                'Authorization': authHeader,
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const result = await response.json();
        input = result.result.toString();
        updateDisplay();
    } catch (error) {
        console.error('Fetch error:', error.message);
        input = 'Error';
        updateDisplay();
    }
}

// History Functions
async function Get_History() {
    const username = sessionStorage.getItem('username');
    const password = sessionStorage.getItem('password');

    if (!username || !password) {
        alert('You are not logged in. Please log in first.');
        window.location.href = 'login.html';
        return;
    }

    const authHeader = `Basic ${btoa(username + ":" + password)}`;

    try {
        const response = await fetch('http://localhost:8080/api/history', {
            method: 'GET',
            headers: {
                "Authorization": authHeader,
                "Content-Type": "application/json"
            },
            credentials: "include"
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const history = await response.json();
        document.getElementById('display').innerText = JSON.stringify(history, null, 2);
    } catch (error) {
        console.error('Fetch error:', error.message);
        document.getElementById('display').innerText = 'Error fetching history';
    }
}

// Logout Function
function logout() {
    sessionStorage.removeItem('username');
    sessionStorage.removeItem('role');
    window.location.href = 'login.html';
}

// Attach logout function to the logout button
const logoutButton = document.querySelector('.logout-btn');
if (logoutButton) {
    logoutButton.addEventListener('click', logout);
}