/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxServerSide.ejb;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafxServerSide.entity.Proyecto;
import javafxServerSide.exception.ConsultaProyectoException;
import javafxServerSide.exception.DeleteProyectoException;
import javafxServerSide.exception.EditProyectoException;
import javafxServerSide.exception.NewProyectoException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Iker Jon Mediavilla
 */
@Stateless
public class ProyectoEJB implements ProyectoEJBLocal {
    
    //logger for the class
    private static final Logger logger =
    Logger.getLogger("javafxServerSide.ejb.ProyectoEJB");
    
    @PersistenceContext
    private EntityManager em;
    
    public Proyecto findProyectoById(Integer id) throws ConsultaProyectoException{
         logger.info("ProyectoEJB: Finding proyecto by id.");
        return em.find(Proyecto.class, id);
    } 
    
    @Override
    public void newProyecto(Proyecto proyecto) throws NewProyectoException{
        logger.info("ProyectoEJB: New Proyecto.");
        //validates data before inserting
      
        /*if(proyecto.getId()==null || proyecto.getCliente()==null || proyecto.getConcepto() == null || proyecto.getHorasEstimadas() == null ||
            proyecto.getImporteEstimado() == null || proyecto.getFechaEstimada() == null){
            logger.severe("ProyectoEJB: ConsultaProyectoException creating Exception ");
            throw new NewProyectoException();
       
        }*/
         try{
             em.persist(proyecto); 
        }catch(Exception e){
            logger.log(Level.SEVERE, "ProyectoEJB: ConsultaProyectoException creating Exception.", e.getMessage());
            throw new NewProyectoException();
        }
    }
    
    public Collection<Proyecto> getAllProyectos() throws ConsultaProyectoException{
        try{
            logger.info("ProyectoEJB: Getting all proyectos.");
            Collection p = em.createNamedQuery("findAllProyectos").getResultList(); 
            return p;
        }catch(Exception e){
             logger.log(Level.SEVERE, "ProyectoEJB: Exception getting all proyectos.{0}", e.getMessage());
            throw new ConsultaProyectoException(e.getMessage());
        }

    }
    
    public void deleteProyecto(Proyecto proyecto) throws DeleteProyectoException{
        try{
            logger.info("ProyectoEJB: Deleting proyecto.");
            //validates data before deleting 
            proyecto=em.merge(proyecto);
            em.remove(proyecto);
        }catch(Exception e){
            logger.log(Level.SEVERE, "ProyectoEJB: Exception deleting proyecto.{0}", e.getMessage());
            throw new DeleteProyectoException(e.getMessage());
        }
        logger.info("ProyectoEJB: Proyecto deleted.");
    }
    
    public void editProyecto(Proyecto proyecto) throws EditProyectoException{
        try{
            logger.info("ProyectoEJB: Editing proyecto.");
            if(!em.contains(proyecto)){
                proyecto = em.merge(proyecto);
            }
        }catch(Exception e){
            logger.log(Level.SEVERE, "ProyectoEJB: Exception editing proyecto.{0}", e.getMessage());
            throw  new EditProyectoException(e.getMessage());
        }
        logger.info("ProyectoEJB: Proyecto edited.");
    }
    
    public Collection<Proyecto> getProyectosFiltrados(int tipo, String cif) throws ConsultaProyectoException{
        try{
            Collection p;
            logger.info("ProyectoEJB: Filtering Proyectos.");
            switch (tipo){
                case 0:
                    p = em.createNamedQuery("findProyectosSinFinalizar").getResultList();
                    break;
                    
                case 1:
                    p = em.createNamedQuery("findProyectosFinalizados").getResultList();
                    break;
                    
                case 2:
                    p = em.createNamedQuery("findAllProyectos").getResultList();
                    break;
                    
                case 3:
                    p = em.createNamedQuery("findProyectosCIF").setParameter("cif", cif).getResultList();
                    break;
                    
                case 4:
                    p = em.createNamedQuery("findProyectosSinFinalizarCIF").setParameter("cif", cif).getResultList();
                    break;
                    
                case 5:
                    p = em.createNamedQuery("findProyectosFinalizadosCIF").setParameter("cif", cif).getResultList();
                    break;
                    
                default:
                    p = em.createNamedQuery("findProyectosSinFinalizar").getResultList();
                    break;
            }
            return p;
        }catch(Exception e){
            throw new ConsultaProyectoException(e.getMessage());
        }
    }
    
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
