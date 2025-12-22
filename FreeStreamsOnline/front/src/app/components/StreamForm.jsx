import { useEffect, useState } from 'react';
import * as CategoryAPI from '../api/category';
import * as PlaylistAPI from '../api/playlist';
import * as API from '../api/stream';
import { validatePublicationDate } from '../validation/publicationDateValidator';

export default function StreamForm({ id, onSuccess }) {
    const [name, setName] = useState('');
    const [image, setImage] = useState('');
    const [description, setDesc] = useState('');
    const [playlistId, setPlaylist] = useState('');
    const [categoryIds, setCategoryIds] = useState([]);
    const [playlists, setPlaylists] = useState([]);
    const [categories, setCategories] = useState([]);
    const [error, setError] = useState('');

    console.log('StreamForm - id:', id);

    useEffect(() => {
        console.log('useEffect triggered, id:', id);

        CategoryAPI.fetchCategories().then(setCategories);
        PlaylistAPI.fetchPlaylists().then(setPlaylists);

        if (id) {
            API.fetchStream(String(id)).then((s) => {
                console.log('Fetched stream for editing:', s);

                setName(s.name || '');
                setImage(s.image || '');
                setDesc(s.description || '');
                setPlaylist(String(s.playlist?.id || ''));
                
                // Извлекаем все ID категорий из массива categories
                if (s.categories && Array.isArray(s.categories)) {
                    const ids = s.categories.map(cat => String(cat.id));
                    setCategoryIds(ids);
                    console.log('Setting category IDs:', ids);
                } else {
                    setCategoryIds([]);
                }
            }).catch(error => {
                console.error('Error fetching stream:', error);
            });
        }
    }, [id]);

    const generateRandomViews = () => {
        const min = 100;
        const max = 1000000;
        return Math.floor(Math.random() * (max - min + 1)) + min;
    };

    const getCurrentDate = () => {
        return new Date().toISOString().split('T')[0];
    };

    async function handleSubmit(e) {
        e.preventDefault();
        setError('');    

        const randomViews = generateRandomViews();
        const currentDate = getCurrentDate();
        const dateValidation = validatePublicationDate(currentDate);
        
        if (!dateValidation.isValid) {
            setError(`Ошибка даты: ${dateValidation.error}`);
            return;
        }
        
        // Преобразуем строковые ID в числа для отправки на сервер
        const numericCategoryIds = categoryIds.map(id => Number(id));
        
        const stream = { 
            name, 
            image, 
            description, 
            views: randomViews, 
            publication_date: currentDate, 
            playlistId: Number(playlistId), 
            categoryIds: numericCategoryIds
        };
        
        console.log('Submitting stream data:', stream);
        
        try {
            if (id) {
                await API.updateStream(String(id), stream);
            } else {
                await API.createStream(stream);
            }
            onSuccess();
        } catch (error) {
            console.error('Error submitting stream:', error);
            alert('Ошибка при сохранении: ' + error.message);
        }
    }

    function handleCategoryChange(categoryId) {
        setCategoryIds(prev => {
            const stringId = String(categoryId);
            if (prev.includes(stringId)) {
                // Удаляем категорию если она уже выбрана
                return prev.filter(id => id !== stringId);
            } else {
                // Добавляем категорию
                return [...prev, stringId];
            }
        });
    }

    function handleFileChange(e) {
        const file = e.target.files[0];
        if (!file) return;

        const reader = new FileReader();
        reader.onloadend = () => {
            // @ts-ignore
            setImage(reader.result);
        };
        reader.readAsDataURL(file);
    }

    return (
        <form onSubmit={handleSubmit} className="container py-4">
            <h2>{id ? 'Редактировать' : 'Добавить'} трансляцию</h2>
            {error && (
                <div className="alert alert-danger">
                    <i className="bi bi-exclamation-triangle me-2"></i>
                    {error}
                </div>
            )}
            
            <div className="mb-3">
                <label className="form-label">Название <span className="text-danger">*</span></label>
                <input
                    type="text"
                    className="form-control"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    required
                />
            </div>

            <div className="mb-3">
                <label className="form-label">Описание <span className="text-danger">*</span></label>
                <textarea
                    className="form-control"
                    value={description}
                    onChange={(e) => setDesc(e.target.value)}
                    required
                    rows={3}
                ></textarea>
            </div>

            <div className="mb-3">
                <label className="form-label">Изображение</label>
                <input
                    type="file"
                    className="form-control"
                    onChange={handleFileChange}
                    accept="image/*"
                />
                <div className="form-text">
                    Поддерживаемые форматы: JPG, PNG, GIF.
                </div>
            </div>
            
            {image && (
                <div className="mb-3">
                    <label className="form-label">Предпросмотр:</label><br />
                    <img 
                        src={image} 
                        alt="preview" 
                        className="img-thumbnail" 
                        style={{ maxHeight: "200px", maxWidth: "300px" }} 
                    />
                </div>
            )}

            <div className="mb-3">
                <label className="form-label">Плейлист <span className="text-danger">*</span></label>
                <select
                    className="form-select"
                    value={playlistId}
                    onChange={(e) => setPlaylist(e.target.value)}
                    required
                >
                    <option value="">Выберите плейлист</option>
                    {playlists.map((p) => (
                        <option key={p.id} value={String(p.id)}>
                            {p.name}
                        </option>
                    ))}
                </select>
            </div>

            <div className="mb-3">
                <label className="form-label">Категории <span className="text-danger">*</span></label>
                <div className="form-text mb-2">
                    Выберите одну или несколько категорий
                </div>
                
                <div className="category-checkboxes mt-2">
                    {categories.map((category) => (
                        <div key={category.id} className="form-check">
                            <input
                                className="form-check-input"
                                type="checkbox"
                                id={`category-${category.id}`}
                                checked={categoryIds.includes(String(category.id))}
                                onChange={() => handleCategoryChange(category.id)}
                            />
                            <label className="form-check-label" htmlFor={`category-${category.id}`}>
                                {category.name} {category.ageLimit ? `(${category.ageLimit}+)` : ''}
                            </label>
                        </div>
                    ))}
                </div>
               
                
                <div className="mt-2">
                    <small className="text-muted">
                        Выбрано категорий: {categoryIds.length}
                        {categoryIds.length > 0 && (
                            <span className="ms-2">
                                ({categoryIds.map(id => {
                                    const cat = categories.find(c => String(c.id) === id);
                                    return cat ? cat.name : id;
                                }).join(', ')})
                            </span>
                        )}
                    </small>
                </div>
            </div>

            <div className="d-flex gap-2">
                <button type="submit" className="btn btn-primary">
                    {id ? 'Сохранить изменения' : 'Создать трансляцию'}
                </button>
                <button 
                    type="button" 
                    className="btn btn-secondary"
                    onClick={onSuccess}
                >
                    Отмена
                </button>
            </div>
        </form>
    );
}