console.log("Contacts.js");
const baseUrl = "http://localhost:8080";
const viewContactModal=document.getElementById('view_contact_modal');

// options with default values
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
  id: 'view_contact_modal',
  override: true
};


const contactModal=new Modal(viewContactModal, options, instanceOptions);

function generateDynamicImage(imageSrc) {
    // Create a new <img> element
    const imgElement = document.createElement('img');
    
    // Set the src attribute dynamically
    imgElement.src = imageSrc;
    
    // Optionally, set other attributes
    imgElement.alt = 'Dynamically generated image';
    imgElement.width = 300; // Set width
    imgElement.height = 200; // Set height

    // Append the image to a container in the DOM
    const container = document.getElementById('imageContainer');
    container.appendChild(imgElement);
}

function openContactModal() {
    contactModal.show();
}

function closeContactModal() {
    contactModal.hide();
}

async function loadContactdata(id) {


    //function call to load data

    try {
        console.log(id);
        const data = await (
        await fetch(`${baseUrl}/api/contacts/${id}`)
        ).json();
        console.log(data);

        // Select the <img> element
        const imgElement = document.querySelector("#contact_image");

        // Dynamically set the src attribute
        imgElement.src = data.picture;

        document.querySelector("#contact_name").innerHTML=data.name;
        // document.querySelector("#image_shown").innerHTML=data.picture;
        document.querySelector("#number").innerHTML=data.phoneNumber;
        document.querySelector("#contact_email").innerHTML=data.email;
        document.querySelector("#contact_address").innerHTML=data.address;
        document.querySelector("#contact_website").innerHTML=data.websiteLink;
        document.querySelector("#contact_linkedIn").innerHTML=data.linkedInLink;
        document.querySelector("#contact_des").innerHTML=data.description;
        openContactModal();
    } catch (error) {
        console.log("Error: ", error);
    }
        
}

//delete contact
async function deleteContact(id) {
    Swal.fire({
        title: "Are you sure?",
        text: "You want to delete this account",
        icon: "warning",
        showCancelButton: true,
        confirmButtonColor: "#3085d6",
        cancelButtonColor: "#d33",
        confirmButtonText: "Yes, delete it!"
      }).then((result) => {
        if (result.isConfirmed) {
          const url=`${baseUrl}/user/contacts/delete/` + id;
          window.location.replace(url);
        }
      });
}