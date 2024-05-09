import { useNavigate } from 'react-router-dom';
import styles from './Success.module.css';
import Button from '../../components/Button/Button';

export function Success() {
	const navigate = useNavigate();

	return (
		<div className={styles['success']}>
			<img className={styles['success-icon']} src="/success-icon.svg" alt="Success icon" />
			<div className={styles['text']}>Your order has been successfully completed!</div>
			<Button appearence="big" onClick={() => navigate('/')}>Make a new one</Button>
		</div>
	);
}