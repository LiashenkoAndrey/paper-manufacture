
let priceFromInput = document.getElementById("priceFrom");
let priceToInput = document.getElementById("priceTo");

function searchGoods() {
    let selectedProducers = getSelectedProducers();
    let priceFrom = priceFromInput.value;
    let priceTo = priceToInput.value;

    let url = document.location.href.split("?")[0];
    if (priceFrom !== 0 && priceTo && selectedProducers.length !== 0) {
        RequestService.doRedirect(`${url}?priceFrom=${priceFrom}&priceTo=${priceTo}&producerIds=${encodeURIComponent(JSON.stringify(selectedProducers))}`)
    } else if (priceFrom !== 0 && priceTo) {
        RequestService.doRedirect(`${url}?priceFrom=${priceFrom}&priceTo=${priceTo}`)
    } else if ( selectedProducers.length !== 0) {
        RequestService.doRedirect(`${url}?producerIds=${encodeURIComponent(JSON.stringify(selectedProducers))}`)
    }
}

function getSelectedProducers() {
    let producerCheckInputs = document.getElementsByClassName("producerCheckInput");
    let selectedProducers = [];

    for (let i = 0; i < producerCheckInputs.length; i++) {
        if (producerCheckInputs[i].checked) {
            let producerId = producerCheckInputs[i].getAttribute("data-producerId");
            selectedProducers.push(producerId);
        }
    }
    return selectedProducers;
}

function hasPriceFilters() {
    return priceFromInput.value !== 0 && priceToInput.value !== 0;
}

if (getSelectedProducers().length !== 0) {
    document.getElementById("producerFilterBtn").click();
}
if (hasPriceFilters()) {
    document.getElementById("priceFilterBtn").click();
}