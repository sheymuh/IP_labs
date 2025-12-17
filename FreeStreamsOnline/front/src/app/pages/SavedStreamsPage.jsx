export const SavedStreamsPage = () => {
    return (
        <main className="flex-grow-1 pt-2">
            <h2>Вам понравилось <i className="bi bi-balloon-heart"></i></h2>
            <div className="photo-grid-container d-flex justify-content-center">
                <div className="photo-grid d-flex align-items-center flex-wrap w-100" id="savedImagesGrid">
                    <div className="photo-grid-item"><img src="/2016.jpeg" alt="стрим ксго" /></div>
                    <div className="photo-grid-item"><img src="/асмр человек паук.webp" alt="асмр" /></div>
                    <div className="photo-grid-item"><img src="/резня.jpg" alt="резня" /></div>
                </div>
            </div>
            <h2>Запланированные трансляции <i className="bi bi-calendar-event"></i></h2>
            <div className="photo-grid-container d-flex justify-content-center">
                <div className="photo-grid d-flex align-items-center flex-wrap w-100">
                    <div className="photo-grid-item"><img src="/стрим ксго.webp" alt="стрим ксго" /></div>
                    <div className="photo-grid-item"><img src="/goats.png" alt="goats" /></div>
                    <div className="photo-grid-item"><img src="/папаня.jpg" alt="папаня" /></div>
                </div>
            </div>
        </main>
    )
}