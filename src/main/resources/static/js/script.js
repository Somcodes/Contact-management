console.log("script loaded");

//change theme work
let currentTheme = getTheme();
//initial -->

document.addEventListener('DOMContentLoaded', () => {
    changeTheme();
})

function changeTheme() {
    //set to web page
    changePageTheme(currentTheme, currentTheme);    
    //set the listner to change theme button
    const changeThemeButton = document.querySelector('#theme_change_button');
    changeThemeButton.addEventListener("click", (event) => {
        let oldTheme = currentTheme;
        console.log("change theme button clicked");
        
        if(currentTheme==="dark") {
            currentTheme="light";
        }
        else {
            currentTheme="dark";
        }
        
        changePageTheme(currentTheme, oldTheme);
    }
    );
}


//set theme to local storage
function setTheme(theme) {
    localStorage.setItem("theme", theme);
}



//get theme from local storage
function getTheme() {
    let theme=localStorage.getItem("theme");
    if(theme)
    return theme;
    else
    return "light";
}

//change current page theme
function changePageTheme(theme, oldTheme) {
//local storage me update karenge
setTheme(currentTheme);
//remove current theme
if(oldTheme) {
    document.querySelector('html').classList.remove(oldTheme);
}
//set current theme
document.querySelector('html').classList.add(theme);

//change the text of button
document
.querySelector("#theme_change_button")
.querySelector('span').textContent = 
theme =='light' ? 'dark' : "light";
}





