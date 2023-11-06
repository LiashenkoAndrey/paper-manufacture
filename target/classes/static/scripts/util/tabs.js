

let tabs = document.getElementsByClassName('tabItem');
let btnList = document.getElementsByClassName('goodTypeBtn');

function changeCurrentTab(id, menu) {
    for (let i = 0; i < btnList.length; i++) {
        btnList[i].classList.remove('selectedGoodType');
    }
    menu.classList.add('selectedGoodType')

    for (let i= 0; i < tabs.length; i++) {
        tabs[i].style.display = 'none';
    }
    document.getElementById(id).style.display = 'flex'
}
