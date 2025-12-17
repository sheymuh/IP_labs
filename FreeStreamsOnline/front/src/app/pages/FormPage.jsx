import { useNavigate, useParams } from 'react-router-dom';
import StreamForm from '../components/StreamForm';

export const FormPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();

    console.log('FormPage - id:', id);
    
    return <StreamForm id={id} onSuccess={() => navigate('/account')} />;
};
