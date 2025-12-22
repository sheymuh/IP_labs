import { useEffect, useState } from 'react';
import * as CategoryAPI from '../api/category';

export const CategoryPage = () => {
    const [categories, setCategories] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        loadCategories();
    }, []);

    const loadCategories = async () => {
        try {
            setLoading(true);
            const data = await CategoryAPI.fetchCategories();
            setCategories(Array.isArray(data) ? data : []);
        } catch (err) {
            console.error('Error loading categories:', err);
            setError('Не удалось загрузить категории');
            setCategories([]);
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return (
            <main className="flex-grow-1 pt-2">
                <h2>Популярные категории <i className="bi bi-tags"></i></h2>
                <div className="alert alert-info">
                    <div className="spinner-border spinner-border-sm me-2" role="status"></div>
                    Загрузка категорий...
                </div>
            </main>
        );
    }

    if (error) {
        return (
            <main className="flex-grow-1 pt-2">
                <h2>Популярные категории <i className="bi bi-tags"></i></h2>
                <div className="alert alert-danger">
                    <i className="bi bi-exclamation-triangle me-2"></i>
                    {error}
                </div>
                <button 
                    className="btn btn-primary"
                    onClick={loadCategories}
                >
                    Попробовать снова
                </button>
            </main>
        );
    }

    if (categories.length === 0) {
        return (
            <main className="flex-grow-1 pt-2">
                <h2>Популярные категории <i className="bi bi-tags"></i></h2>
                <div className="alert alert-info">
                    <i className="bi bi-info-circle me-2"></i>
                    Категории не найдены
                </div>
            </main>
        );
    }

    return (
        <main className="flex-grow-1 pt-2">
            <h2>Популярные категории <i className="bi bi-tags"></i></h2>
            
            <div className="row">
                {categories.map((category) => (
                    <div key={category.id} className="col-md-6 col-lg-4 mb-4">
                        <div className="card category-card h-100">
                            <div className="card-body">
                                <h5 className="card-title">
                                    {category.name}
                                    {category.ageLimit > 0 && (
                                        <span className="badge bg-warning text-dark ms-2">
                                            {category.ageLimit}+
                                        </span>
                                    )}
                                </h5>                                    
                                    <div className="mb-2">
                                        <span className="badge bg-secondary">
                                            Возрастное ограничение: {category.ageLimit || '0'}+
                                        </span>
                                    </div>
                                </div>
                            </div>
                        </div>
                ))}
            </div>
        </main>
    );
};