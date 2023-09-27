

function showTab(elem) {
    rotateArrow(elem, "90");
    let tab = elem.parentNode.getElementsByClassName("tab")[0];
    tab.style.display = 'flex';
    elem.onclick = function() {
        hideTab(elem)

    }
}


function hideTab(elem) {
    rotateArrow(elem,"0");
    let tab = elem.parentNode.getElementsByClassName("tab")[0];
    tab.style.display = 'none';
    elem.onclick = function() {
        showTab(elem)
    }
}

function rotateArrow(elem, deg) {
    let arrow = elem.parentNode.parentNode.getElementsByTagName("img")[0];
    arrow.style.transform = "rotate("+ deg +"deg)";
}