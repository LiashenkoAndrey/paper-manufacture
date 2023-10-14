
import {GoodPrinter, PAGE_SIZE} from "/scripts/GoodPrinter.js";

let loadGoodBtn = document.getElementById("loadGoodBtn");

 function getGoodsAndPrintByPage() {
    let pageId = loadGoodBtn.getAttribute("data-pageId");
    let params = `?pageId=${String(pageId)}&pageSize=${String(PAGE_SIZE)}`;
    GoodPrinter.getGoodsAndPrintByPage("/good/manufacture-machine/all", params);
    loadGoodBtn.setAttribute("data-pageId", Number.parseInt(pageId) + 1);
}

window.getGoodsAndPrintByPage = getGoodsAndPrintByPage;