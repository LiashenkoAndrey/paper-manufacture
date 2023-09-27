
export const PAGE_SIZE = 5;

export class GoodPrinter {
    static printGoods(goods) {
        let goodsWrapper = document.getElementsByClassName("goods")[0];
        for (let i = 0; i < goods.length; i++) {
            console.log(goods[i])
            goodsWrapper.appendChild(this.createGoodHtml(goods[i]));
        }

        if (goods.length < PAGE_SIZE) {
            document.getElementById("loadGoodBtn").style.display = 'none'
        }
    }

    static createGoodHtml(good) {
        let goodHtml =
             `
            <a href="/producer/${good.producerId}">
                <img width="60" src="/upload/image/${good.producerLogotypeId}" alt="">
            </a>
            <img class="goodImage" height="120" src="/upload/image/${good.imageId}"  alt="">
            <span class="serialNumber">${good.serialNumber}</span>
            <span class="text-black">${good.name}</span>
            <span class="mt-2" style="font-weight: bold">An approximate price: ${good.price}</span>
            <a href="/good/manufacture-machine?id=${good.id}" class="link text-black mt-2">Product details</a>
            
            `

        let goodWrapper = document.createElement("div")
        goodWrapper.classList.add("good");
        goodWrapper.innerHTML = goodHtml;
        return goodWrapper;
    }

    static getGoodsAndPrintByPage(url, params) {
        fetch(url + params, {
            method: "GET"
        }).then((response) => response.json())
            .then((goodsList) => {
                GoodPrinter.printGoods(goodsList)
            });
    }
}