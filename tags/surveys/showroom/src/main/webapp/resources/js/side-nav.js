window.onload = function() {
	var id='';
	
	try{
		/*${navigatorBean.setShowroom('zugang_start')};*/
	id = localStorage.getItem("showCurrentElement");
	
	/*id=""+${navigatorBean.getShowroom};*/
	}catch(e) {
	id = 'willkommen';
	localStorage.setItem("showCurrentElement",id);
	}
	manageAccordion(id);
	}
	   function openSideNav() {
           document.getElementById("sidenav").v  .style.display = "block";
       }
       function closeSideNav() {
           document.getElementById("sidenav").style.display = "none";
       }
       function manageAccordion(id) {
       document.getElementById("sidenav").className.replace(" w3-show", "");
       localStorage.setItem("showCurrentElement",id);
           var x = document.getElementById(localStorage.getItem("showCurrentElement"));
            x.className += " w3-show";
            x.previousElementSibling.className += " w3-blue";
       }