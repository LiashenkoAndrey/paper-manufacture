

let tabs = document.getElementsByClassName('tabItem');

let lastTab;
let lastBtn


function changeCurrentTab(id, menu) {
    console.log(id)
    for (let i= 0; i < tabs.length; i++) {
        tabs[i].style.display = 'none';
    }

    menu.classList.remove('underlined');

    if ( lastBtn !== undefined) {
        lastBtn.style.cursor = 'pointer';
        menu.classList.add('underlined')
    }

    lastBtn = menu;

    let line = menu.querySelector("div .goodTypeBtnHeadLine");
    line.style.display = 'none';
    if (lastTab !== undefined) {
        lastTab.style.display = 'block'
    }

    lastTab = line;
    document.getElementById(id).style.display = 'flex'
}
