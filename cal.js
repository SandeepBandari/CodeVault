let input = '';

function updateDisplay() {
    document.getElementById('display').innerText = input || '0';
  }

function prs_key(key) {
    input += key;
    updateDisplay(); // Update "display"
}

function del() {
    input = input.slice(0, -1);
    updateDisplay();
}

function clearDisplay() {
    input = '';
    updateDisplay();
}

function storeCalculationResult(result)
 {
    let history = JSON.parse(sessionStorage.getItem('calcHistory')) || [];
    history.push(result);
    sessionStorage.setItem('calcHistory', JSON.stringify(history));
}

function displayHistory()
 {
    let history = JSON.parse(sessionStorage.getItem('calcHistory')) || [];
    let historyContainer = document.getElementById('history');
    historyContainer.innerHTML = '';
    history.forEach((item, index) => {
        historyContainer.innerHTML += `<p>${index + 1}. ${item.value1} ${item.operationtype} ${item.value2} = ${item.result}</p>`;
    });
}

function addToHistoryManually(value1, value2, operation, result) 
{
    let calculation = { value1, value2, operationtype: operation, result };
    storeCalculationResult(calculation);
    displayHistory();
}

window.onload = displayHistory;
addToHistoryManually(60, 30, 'sub', 30);



