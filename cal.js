let input = ''; 

function prs_key(key) {
    input += key; 
    updateDisplay(); 
}

function updateDisplay() {
    document.getElementById('display').innerText = input || '0';
}
function showHistory() {
    let history = JSON.parse(sessionStorage.getItem('calcHistory')) || [];
    let historyText = history.map((item, index) => 
        `${index + 1}. ${item.value1} ${item.operationtype} ${item.value2} = ${item.result}`
    ).join('\n'); 
    document.getElementById('display').innerText = historyText || 'No history available';
}
function clearDisplay() {
    input = ''; 
    updateDisplay(); 
}

function del() {
    input = input.slice(0, -1); 
    updateDisplay(); 
}
function storeCalculationResult(result) {
    let history = JSON.parse(sessionStorage.getItem('calcHistory')) || [];
    history.push(result);
    sessionStorage.setItem('calcHistory', JSON.stringify(history));
}

function updateDisplay() {
    document.getElementById('display').innerText = input || '0';
}

async function calculate() {
    const operationMap = { '+': 'add', '-': 'sub', '*': 'mul', '/': 'div', '%': 'modulus' };
    const operation = input.match(/[+\-*/%]/)?.[0];
    if (!operation) return alert('Invalid operation');

    const [value1, value2] = input.split(operation).map(Number);
    const operationName = operationMap[operation];

    try {
        const response = await fetch(`http://localhost:8080/calculator/${operationName}?value1=${value1}&value2=${value2}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);

        const result = await response.json();
        input = result.result.toString();
        updateDisplay();
        storeCalculationResult(result); 
    } catch (error) {
        console.error('Fetch error:', error.message);
    }
}

window.onload = function () {
    updateDisplay();
};

async function Get_History() {
    try {
        const response = await fetch('http://localhost:8080/calculator/history', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) throw new Error(`HTTP error! Status: ${response.status}`);

        const history = await response.json();

        let historyText = history.map((item, index) =>
            `${index + 1}. ${item.value1} ${item.operationtype} ${item.value2} = ${item.result}`
        ).join('\n');

        document.getElementById('display').innerText = historyText || 'No DB history available';
    } catch (error) {
        console.error('Fetch error:', error.message);
        document.getElementById('display').innerText = 'Error fetching DB history';
    }
}
