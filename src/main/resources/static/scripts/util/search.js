

let searchInput = document.getElementById("searchInput");

let serialNumbers = new Map();

function getSerialNumbers() {
    console.log("click")
    fetch("http://localhost/good/manufacture-machine/serial_numbers/all", {
        method: "GET"
    }).then((res) => res.json())
        .then((res) => {
            console.log(res);
            serialNumbers = new Map(Object.entries(res));
        })

    searchInput.removeEventListener("click", getSerialNumbers);
}

searchInput.addEventListener("click", getSerialNumbers);

let searchListDisabled = true;

searchInput.addEventListener("keypress", ev => {
    document.body.addEventListener("click", function () {
        disableSearchedList()
    });

    clearSearchedList();

    let value = searchInput.value;
    let keys = Array.from( serialNumbers.keys() );
    let searched = [];
    for (let i = 0; i < keys.length; i++) {
        if (keys[i].toLowerCase().includes(value.toLowerCase())) {
            searched.push(keys[i]);
        }
    }
    if (searched.length !== 0) {
        displaySearchedValues(searched);
    } else {
        disableSearchedList()
    }
});

let searchedList = document.getElementById("searchedList")

function disableSearchedList() {
    searchedList.style.display = "none";
    searchListDisabled = true;
}

function enableSearchedList() {
    searchedList.style.display = "block";
    searchListDisabled = false;
}

function clearSearchedList() {
    searchedList.innerHTML = "";
}

function displaySearchedValues(vals) {
    if (searchListDisabled) enableSearchedList()

    for (let i = 0; i < vals.length; i++) {
        let link = document.createElement("a");
        link.innerHTML = vals[i];
        link.classList.add("link");
        link.classList.add("d-block")
        link.href = "http://localhost/good/manufacture-machine?id=" + serialNumbers.get(vals[i]);
        searchedList.appendChild(link);
    }
}