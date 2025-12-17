import { useState } from 'react';
import { Link } from 'react-router-dom';

export const MainPage = () => {
    const [savedImages, setSavedImages] = useState([]);

    const initialStreams = [
        {
            id: 'cs2',
            name: 'CS2',
            image: 'https://steamuserimages-a.akamaihd.net/ugc/2462990917964003785/9E09A87AE9B299BC1F0FC1CBA9F20DB16289442A/?imw=512&imh=298&ima=fit&impolicy=Letterbox&imcolor=%23000000&letterbox=true'
        },
        {
            id: 'derzko',
            name: 'Dersko',
            image: 'https://avatars.mds.yandex.net/i?id=7839e8d6f40309bdce67fac62990d108_sr-10636981-images-thumbs&n=13'
        },
        {
            id: 'stardew',
            name: 'Stardew Valley',
            image: 'https://vkplay.ru/pre_0x736_resize/hotbox/content_files/news/2020/02/12/fe52b98a1367439ea2be293fcf48224a.jpg?quality=85'
        },
        {
            id: 'teddy',
            name: 'Teddy',
            image: 'https://avatars.mds.yandex.net/i?id=c111e02e6999cca7c4a7aa47f00a2ab3c384b6dd-5884537-images-thumbs&n=13'
        },
        {
            id: 'lofi_girl',
            name: 'Lofi Girl',
            image: 'https://i.pinimg.com/736x/a8/f1/c0/a8f1c04546867fbcd5eccd41c115fb51.jpg'
        }
    ];

    const popularChannels = [
        { name: 'ВЫ самый популярный стример на данной платформе!!!', link: '/account' },
        { name: 'какой-то стример 1' },
        { name: 'какой-то стример 2' },
        { name: 'ммм МАРМОК' }
    ];

    const handleSaveStreams = (event) => {
        event.preventDefault();
        const formData = new FormData(event.target);
        const selectedImages = Array.from(formData.getAll('images'));
        
        setSavedImages(selectedImages.map(url => ({ url })));
    };

    return (
            <main className="flex-grow-1 pt-2">
                <h2>Сейчас в эфире <i className="bi bi-cast"></i></h2>
                
                <form id="imageForm" className="mb-4" onSubmit={handleSaveStreams}>
                    <div className="photo-grid-container d-flex justify-content-center mb-2">
                        <div className="photo-grid d-flex align-items-center flex-wrap w-100">
                            {initialStreams.map(stream => (
                                <div key={stream.id} className="photo-grid-item">
                                    <input
                                        type="checkbox"
                                        name="images"
                                        value={stream.image}
                                        id={stream.id}
                                    />
                                    <label htmlFor={stream.id}>
                                        <img src={stream.image} alt={stream.name} />
                                    </label>
                                </div>
                            ))}
                        </div>
                    </div>
                    <button type="submit" className="btn btn-primary">Смотреть позже</button>
                </form>

                <h3>Популярные каналы <i className="bi bi-patch-check-fill"></i></h3>
                <ul>
                    {popularChannels.map((channel, index) => (
                        <li key={index}>
                            {channel.link ? (
                                <Link to={channel.link}>
                                    <em>{channel.name}</em>
                                </Link>
                            ) : (
                                channel.name
                            )}
                        </li>
                    ))}
                </ul>

                <h2>Смотреть позже <i className="bi bi-clock-fill"></i></h2>
                <div className="photo-grid-container d-flex justify-content-center">
                    <div className="photo-grid d-flex align-items-center flex-wrap w-100" id="savedImagesGrid">
                        {savedImages.length > 0 ? (
                            savedImages.map((image, index) => (
                                <div key={index} className="photo-grid-item">
                                    <img src={image.url} alt="сохраненное изображение" />
                                </div>
                            ))
                        ) : (
                            <div className="photo-grid-item">
                                <img
                                    src="https://sun9-27.userapi.com/impf/c9811/u99622377/d_9475926f.jpg?quality=96&as=50x50,100x100&sign=a4fcc81d8c851f41a7f85dea825afc66&u=pHWjezk_9pOPyRtoH8161qsxD963pzSE2bk8P8vDAyE&cs=100x100"
                                    alt="pusto"
                                />
                            </div>
                        )}
                    </div>
                </div>
            </main>
    );
};