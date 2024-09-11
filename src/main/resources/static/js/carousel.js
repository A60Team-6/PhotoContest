const carouselInner = document.querySelector('.carousel-inner');
const prevButton = document.querySelector('.carousel-prev');
const nextButton = document.querySelector('.carousel-next');

let currentIndex = 0;
const itemsPerSlide = 4;  // Показване на 4 снимки едновременно
const totalItems = document.querySelectorAll('.category-item').length;

nextButton.addEventListener('click', () => {
    if (currentIndex < totalItems - itemsPerSlide) {
        currentIndex++;
        updateCarousel();
    }
});

prevButton.addEventListener('click', () => {
    if (currentIndex > 0) {
        currentIndex--;
        updateCarousel();
    }
});

function updateCarousel() {
    const offset = -currentIndex * (100 / itemsPerSlide);  // Преместване с 25% на всеки елемент
    carouselInner.style.transform = `translateX(${offset}%)`;
}
