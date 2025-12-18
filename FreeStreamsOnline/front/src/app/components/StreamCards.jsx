export const StreamCards = ({ stream, onEdit, onDelete }) => {
    // Функция для форматирования списка категорий
    const formatCategories = () => {
        if (!stream.categories || stream.categories.length === 0) {
            return "Без категории";
        }
        return stream.categories.map(cat => cat.name).join(", ");
    };

    return (
        <div className="card stream-card" style={{ maxWidth: '100%' }}>
            <div className="d-flex justify-content-center align-items-center p-3">
                <img 
                    src={stream.image} 
                    className="card-img-top w-100" 
                    alt={stream.name} 
                    style={{ 
                        height: '250px',
                        objectFit: 'cover'
                    }}
                />
            </div>
            <div className="card-body d-flex flex-column">
                <h5 className="card-title">Название: {stream.name}</h5>
                <h6 className="card-subtitle text-muted">Описание: {stream.description}</h6>
                <p className="card-text">
                    Плейлист: <strong>{stream.playlist?.name || "Не найден"}</strong>
                </p>
                <p className="card-text">
                    Категории: <em>{formatCategories()}</em>
                </p>
                <div className="mt-auto">
                    <div className="card-buttons d-flex gap-2">
                        <button className="btn btn-warning" onClick={onEdit}>
                            Редактировать
                        </button>
                        <button className="btn btn-danger" onClick={onDelete}>
                            Удалить
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};