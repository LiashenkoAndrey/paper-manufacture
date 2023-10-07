import {setCookie} from "/scripts/cookies.js";


export function setLanguage(element) {
    let lang = element.getAttribute("data-lang");
    setCookie("lang", lang);
    window.location.reload();
}

window.setLanguage = setLanguage;
