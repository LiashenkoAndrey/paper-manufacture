import {GoodPrinter, PAGE_SIZE} from "/scripts/GoodPrinter.js";

let loadGoodBtn = document.getElementById("loadGoodBtn");

function loadGood() {
  let priceFromInput = document.getElementById("priceFrom");
  let priceToInput = document.getElementById("priceTo");
  let pageId = loadGoodBtn.getAttribute("data-pageId");
  let producerIds = getSelectedProducers();
  let params = new URLSearchParams();

  params.append('pageId', pageId);
  params.append('pageSize', String(PAGE_SIZE));
  params.append('catalogId', loadGoodBtn.getAttribute("data-cataloId"));

  if (producerIds.length !== 0) {
    params.append('producerIds', JSON.stringify(producerIds));
  }
  if (priceFromInput.value !== 0 && priceToInput.value !== 0) {
    params.append('priceFrom', priceFromInput.value);
    params.append('priceTo', priceToInput.value);
  }
  GoodPrinter.getGoodsAndPrintByPage("/good/manufacture-machine/page?" ,params.toString(), pageId);
  loadGoodBtn.setAttribute("data-pageId", Number.parseInt(pageId) + 1)
}

window.loadGood = loadGood;
