import {getCookie, setCookie} from "/scripts/cookies.js";

export function changeLanguageIfNeeded() {
    let lang = new URLSearchParams(document.location.search).get("lang");
    if (lang == null) {
        let selectedLanguage = getCookie("lang");
        console.log(selectedLanguage)
        if (selectedLanguage !== "") {
            console.log(selectedLanguage);
            window.location.replace(window.location.href + "?lang=" + selectedLanguage);
        }
    }
}

export function setLanguage(element) {
    let lang = element.getAttribute("data-lang");
    setCookie("lang", lang);
}