
let leftRectangle = document.getElementById("leftRectangle");
let rightRectangle = document.getElementById("rightRectangle");
let slider = $( "#slider-range" );

// const yValues = [0,7,8,8,9,9,9,10,11,14,14,15,0];

let sliders = document.getElementById("slider-range").getElementsByTagName("span");


async function getPrices() {
    return  fetch("http://localhost/good/manufacture-machine/allPricesWithAmount", {
        method: "GET"
    }).then((response) =>  response.json());
}

$( async function () {
    await getMaxGoodPriceAndSetMaxPrice()
        .then((response) => {
            let priceTo
            if (priceToInput.value == 0 && priceFromInput.value == 0) {
                priceTo = response.maxPrice
            } else priceTo = priceToInput.value;


            slider.slider({
                range: true,
                min: 0,
                max: response.maxPrice,
                values: [
                    priceFromInput.value,
                    priceTo
                ],
                slide: renderSelectedPriceArea
            });
        })

    await getPrices().then((response) => {
        console.log(response);
        let yValues = [0];
        const xValues = [0];

        for (let i = 0; i < response.length; i++) {
            let priceAndAmount = response[i];
            yValues.push(priceAndAmount.amount)
            xValues.push(priceAndAmount.price)
        }
        xValues.push(0)
        yValues.push(0)

        renderChart(xValues, yValues);
        if (hasPriceFilters()) renderSelectedPriceArea()
    })
});

function renderSelectedPriceArea() {
    leftRectangle.style.width =  getLeftCssProperty(sliders[0]) + "%"
    rightRectangle.style.width = (100 - getLeftCssProperty(sliders[1])) + "%"
    priceFromInput.value = slider.slider( "values", 0 )
    priceToInput.value =  slider.slider( "values", 1 )
}

async function getMaxGoodPriceAndSetMaxPrice() {
    return  fetch("/good/manufacture-machine/maxPrice", {
        method: "GET"
    }).then((response) =>  response.json());
}

function getLeftCssProperty(htmlElem) {
    return htmlElem.style.left.toString().split("%")[0];
}

function renderChart(xValues, yValues) {
    let ctx = document.getElementById("chart");
    let chart = new Chart(ctx.getContext("2d"), {
        type: "line",
        data: {
            labels: xValues,
            datasets: [
                {
                    label: "Price ($)",
                    backgroundColor: "#dbdbdb",
                    borderColor: "#dbdbdb",
                    data: yValues
                }
            ]
        },
        options: {
            title: {
                display: false
            },

            legend: {
                display: false
            },

            scales: {
                xAxes: [{
                    ticks: {
                        display: false
                    },
                    gridLines: {
                        display: false
                    }
                }],
                yAxes: [{
                    ticks: {
                        display: false
                    },
                    gridLines: {
                        display: false
                    }
                }]
            },
            plugins: {

            },
            elements: {
                point:{
                    radius: 0
                },  line: {
                    tension: 0
                }
            }
        }
    });
}