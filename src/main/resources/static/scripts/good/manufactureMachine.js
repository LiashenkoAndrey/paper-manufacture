let createManufactureMachineForm =
    `<div class="modalWrapper">
        <div class="contentWrapper">
            <div>
                <div class="text-end mb-4">
                    <button style="font-size: 20px" type="button" class="btn-close btn-close-white" onclick="FormService.disable(this.parentNode.parentNode.parentNode.parentNode)" aria-label="Close"></button>
                </div>
                <div class="contentBody">
                    <div>
                        <div class="form-group">
                            <label for="name">Name</label>
                            <input id="inputName" class="form-control" type="text">
                        </div>
                        
                        <div class="form-group">
                            <label for="name">Description</label>
                            <textarea cols="4" id="inputDescription" class="form-control" type="text"></textarea>
                        </div>
                        
                        <div class="form-group">
                            <label for="name">Serial number</label>
                            <input id="serialNumber" class="form-control" type="text">
                        </div>            
                        
                        <div class="form-group">
                            <label for="name">Price</label>
                            <input id="inputPrice" class="form-control" type="number">
                        </div>          
                        
                        <div>
                            <label for="images">Images</label>
                            <input id="inputFiles" class="form-control" type="file" multiple="multiple">
                        </div>
                        
                        <div>
                            <label for="catalogSelect">Catalog</label>
                            <select class="form-select"  id="catalogSelect"></select>
                        </div>
                        
                        <div>
                            <label for="producersSelect">Producer</label>
                            <select  class="form-select" id="producersSelect"></select>
                        </div>
                    </div>
        
                    <div id="modalProperties">
                        <h5 class="mx-3 text-center">Properties</h5>
                        <div id="propertyContainer">
                            <div>
                                <input class="key" type="text">
                                <input class="val" type="text">
                            </div>
                            
                            <div>
                                <input class="key" type="text">
                                <input class="val" type="text">
                            </div>
                           
                            <div>
                                <input class="key" type="text">
                                <input class="val" type="text">
                            </div>
                            
                            <div>
                                <input class="key" type="text">
                                <input class="val" type="text">
                            </div>    
                            
                            <div>
                                <input class="key" type="text">
                                <input class="val" type="text">
                            </div>                                                                               
                        </div>
                        <span style="font-size: 54px" id="plusProperty" onclick="addProperty()">+</span>
                    </div>
                </div>
                <button class="btn btn-success mt-3" onclick="save()">Save</button>
            </div>
        </div>
    </div>`

let updateManufactureMachineForm =
    `<div class="modalWrapper">
        <div class="contentWrapper">
           <div>
               <div class="text-end mb-4">
                    <button style="font-size: 20px" type="button" class="btn-close btn-close-white" onclick="FormService.disable(this.parentNode.parentNode.parentNode.parentNode)" aria-label="Close"></button>
                </div>
               <div class="contentBody">
                    <div>
                        <div class="form-group">
                            <label for="name">Name</label>
                            <input id="inputName" class="form-control" type="text">
                        </div>
                        
                        <div class="form-group">
                            <label for="name">Description</label>
                            <textarea cols="4" id="inputDescription" class="form-control" type="text"></textarea>
                        </div>
                        
                        <div class="form-group">
                            <label for="name">Serial number</label>
                            <input id="serialNumber" class="form-control" type="text">
                        </div>                    
                        
                        <div class="form-group">
                            <label for="name">Price</label>
                            <input id="inputPrice" class="form-control" type="number">
                        </div>    
                        
                        <div>
                            <label for="images">Images</label>
                            <input id="inputFiles" class="form-control" type="file" multiple="multiple">
                        </div>
                        
                        <div>
                            <label for="catalogSelect">Catalog</label>
                            <select class="form-select" id="catalogSelect"></select>
                        </div>
                        
                        <div>
                            <label for="producersSelect">Producer</label>
                            <select class="form-select" id="producersSelect"></select>
                        </div>
                    </div>
        
                    <div id="modalProperties">
                        <h5 class="mx-3 text-center">Properties</h5>
                        <div id="propertyContainer"></div>
                        <span style="font-size: 54px" id="plusProperty" onclick="addProperty()">+</span>
                    </div>
                </div>
               <button class="btn btn-success mt-3 mx-5 mb-5" onclick="update()">Update</button>
            </div> 
        </div>
    </div>`

let deleteManufactureMachineForm =
    `<div class="modalWrapper">
        <div class="contentWrapper">
            <div>
                <div class="text-end mb-4">
                <button style="font-size: 20px" type="button" class="btn-close btn-close-white" onclick="FormService.disable(this.parentNode.parentNode.parentNode.parentNode)" aria-label="Close"></button>
            </div>
            <div class="contentBody">
                <h5 style="color: white">Are you sure that you want to delete this video?</h5>
            </div>        
                <button class="btn btn-success mt-3" onclick="deleteManufactureMachine()">Delete</button>
            </div>
        </div>
    </div>`


function displayData() {
    let updateBtn = document.getElementById("updateData");
    let catalogId = updateBtn.getAttribute("data-catalog_id");
    let producerId = updateBtn.getAttribute("data-producer_id");

    copyVal('priceValue', 'inputPrice');
    copyVal('name', 'inputName')
    copyVal('description', 'inputDescription')
    copyVal('serialNumberValue', 'serialNumber')

    getAndDisplayAllCatalogs(catalogId);
    getAndDisplayAllProducers(producerId);
    getAndDisplayAllPropertiesOfManufactureMachine();
}

function getQueryParam(name) {
    return new URLSearchParams(document.location.search).get(name);
}

function copyVal(fromId, toId) {
    console.log(fromId)
    console.log(toId)
    let to = document.getElementById(toId);
    let from = document.getElementById(fromId);
    console.log(from)
    console.log(to)

    to.value = from.innerText;
}

function getAndDisplayAllProducers(selectedId) {
    let select = document.getElementById("producersSelect");
    fetch("/producer/all", {
        method: "GET"
    }).then((response) => {
        contentTypeIsJSON(response);

        response.json().then((producers) => {
            displayEntityList(select, producers, selectedId)
        });
    })
}

function getAndDisplayAllCatalogs(selectedId) {
    let select = document.getElementById("catalogSelect");
    fetch("/good/manufacture-machine/catalog/all", {
        method: "GET"
    }).then((response) => {
        contentTypeIsJSON(response);

        response.json().then((groups) => {
            displayEntityList(select, groups, selectedId)
        });
    })
}

function getAndDisplayAllPropertiesOfManufactureMachine() {
    fetch("/good/manufacture-machine/"+ new URLSearchParams(document.location.search).get("id") +"/properties", {
        method: "GET"
    }).then((response) => {
        contentTypeIsJSON(response);

        response.json().then((properties) => {
            let map = new Map(Object.entries(properties))
            let propertyContainer = document.getElementById("propertyContainer");
            let keys = Array.from(map.keys());
            let values = Array.from(map.values());
            for (let i = 0; i < keys.length; i++) {
                addFilledProperty(propertyContainer, keys[i], values[i])
            }
        });
    })
}

function addFilledProperty(container, key, val) {
    let property = document.createElement("div");
    let keyInput = document.createElement("input");
    keyInput.type = 'text';
    keyInput.classList.add("key");
    keyInput.value = key;
    property.appendChild(keyInput)

    let valInput = document.createElement("input");
    valInput.type = 'text';
    valInput.classList.add("val");
    valInput.value = val;
    property.appendChild(valInput)

    container.appendChild(property);
}

function displayEntityList(wrapper, entities, selectedId) {
    for (let i = 0; i < entities.length; i++) {
        let option = document.createElement("option");
        let entity = entities[i];
        option.text = entity.name;
        option.value = entity.id;

        if (selectedId != null) {
            if (selectedId == entity.id) {
                option.selected = true;
            }
        }
        wrapper.appendChild(option);
    }
}

function contentTypeIsJSON(response) {
    let contentType = response.headers.get("Content-Type");
    if (contentType !== "application/json") {
        throw Error("Unexpected content type: expected: 'application/json' but was '" + contentType + "'");
    }
}


let newProperty =
    `<div>
        <input class="key" type="text">
        <input class="val" type="text">
     </div>`

function addProperty() {
    let container = document.getElementById('propertyContainer');
    container.insertAdjacentHTML('beforeend', newProperty);
}

function parsePropertiesAndReturnAsArray() {
    let keys = document.getElementsByClassName("key");
    let vals = document.getElementsByClassName("val");
    if (keys.length !== vals.length) {
        alert("keys amount differs from values");
        return;
    }
    let properties = new Map();
    for (let i = 0; i < keys.length; i++) {
        let key = keys[i].value;
        let val = vals[i].value;
        if (key !== '' && val !== '') {
            console.log(key+" , "+ val)
            properties.set(key, val);
        }
    }
    return properties;
}

async function deleteManufactureMachine() {
    fetch("/good/manufacture-machine/" + new URLSearchParams(document.location.search).get("id") + "/delete", {
        method: "DELETE",
    }).then((response) => {
        RequestService.processResponseAndDoRedirect(response, "http://localhost/good/manufacture-machine/view/all");
    })
}

async function getData() {
    let name = document.getElementById("inputName").value;
    let description = document.getElementById("inputDescription").value;
    let filesArray = document.getElementById("inputFiles").files;
    let producerId = document.getElementById("producersSelect").value;
    let serialNumber = document.getElementById("serialNumber").value;
    let catalogId = document.getElementById("catalogSelect").value;
    let price = document.getElementById("inputPrice").value;

    let images = [];
    for (let i = 0; i < filesArray.length; i++) {
        await getBase64(filesArray[i]).then((converted) => {
            const type = "image/" + converted.split(';')[0].split('/')[1];
            images.push({
                type: type,
                base64Image: converted
            })
        })
    }

    return {
        manufactureMachine: {
            name: name,
            description: description,
            properties: Object.fromEntries(parsePropertiesAndReturnAsArray()),
            serialNumber: serialNumber,
            price: price
        },
        images: images,
        producerId: producerId,
        catalogId: catalogId
    };
}

async function save() {
    fetch(`/good/manufacture-machine/new`, {
        method: "POST",
        body: JSON.stringify(await getData()),
        headers: {
            "Content-Type": "application/json"
        }
    }).then((response) => {
        response.text().then((response) => {
            RequestService.processResponseAndDoRedirect(response, "http://localhost/good/manufacture-machine?id=" + parseInt(response))
        })
    })
}

async function update() {
    console.log(JSON.stringify(await getData()));
    fetch(`/good/manufacture-machine/${getQueryParam('id')}/update`, {
        method: "PUT",
        body: JSON.stringify(await getData()),
        headers: {
            "Content-Type": "application/json"
        }
    }).then((response) => {
        response.text().then((response) => {
            RequestService.processResponseAndReload(response);
        })
    })
}

async function getBase64(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = error => reject(error);
    });
}



