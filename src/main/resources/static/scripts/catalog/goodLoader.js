

const PAGE_SIZE = 5;

function createGoodHtml(good) {
    let goodHtml =
      `  <a href="/producer/${good.producerId}">
            <img width="60" src="/upload/image/${good.producerLogotypeId}" alt="">
          </a>
          <img class="goodImage" height="120" src="/upload/image/${good.imageId}"  alt="">
          <span class="serialNumber">${good.serialNumber}</span>
          <span class="text-black"${good.name}</span>
          <span class="mt-2" style="font-weight: bold">An approximate price: ${good.price}</span>
          <a href="/good/manufacture-machine?id=${good.id}" class="link text-black mt-2">Product details</a>`

    let goodWrapper = document.createElement("div")
    goodWrapper.classList.add("good");
    goodWrapper.innerHTML = goodHtml;
    return goodWrapper;
}
let loadGoodBtn = document.getElementById("loadGoodBtn");

function loadGood() {
    let pageId = loadGoodBtn.getAttribute("data-pageId");
    let producerIds = getSelectedProducers();

    let params = new URLSearchParams();

    params.append('pageId', pageId);
    params.append('pageSize', "5");
    params.append('catalogId', loadGoodBtn.getAttribute("data-cataloId"));
    if (producerIds.length !== 0) {
        params.append('producerIds', producerIds);
    }

    if (priceFromInput.value !== 0 && priceToInput.value !== 0) {
        params.append('priceFrom', priceFromInput.value);
        params.append('priceTo', priceToInput.value);
    }

    getGoodsAndPrintByPage(params.toString());
    loadGoodBtn.setAttribute("data-pageId", Number.parseInt(pageId) + 1)
}

function printGoods(goods) {
    let goodsWrapper = document.getElementsByClassName("goods")[0];
    for (let i = 0; i < goods.length; i++) {
        console.log(goods[i])
        goodsWrapper.appendChild(createGoodHtml(goods[i]));
    }

    if (goods.length < PAGE_SIZE) {
        loadGoodBtn.style.display = 'none'
    }
}

function getGoodsAndPrintByPage(params) {
    fetch("/good/manufacture-machine/page?" + params, {
        method: "GET"
    }).then((response) => response.json())
        .then((goodsList) => {
            console.log(goodsList)
            printGoods(goodsList)
        });
}